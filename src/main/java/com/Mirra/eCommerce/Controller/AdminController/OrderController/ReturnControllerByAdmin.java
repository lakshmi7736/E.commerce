package com.Mirra.eCommerce.Controller.AdminController.OrderController;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Orders.OrderItem;
import com.Mirra.eCommerce.Models.Orders.OrderStatus;
import com.Mirra.eCommerce.Models.Orders.OrderStatusHistory;
import com.Mirra.eCommerce.Models.Returns.ReturnRequest;
import com.Mirra.eCommerce.Models.Returns.ReturnStatus;
import com.Mirra.eCommerce.Models.Users.Related.Wallet;
import com.Mirra.eCommerce.Models.datas.Product;
import com.Mirra.eCommerce.Service.ImageSerilizrAndDeserilize.SerializeAndDeserialize;
import com.Mirra.eCommerce.Service.OrderManagement.ReturnRequestService;
import com.Mirra.eCommerce.Service.Orders.OrderItemService;
import com.Mirra.eCommerce.Service.Orders.OrderService;
import com.Mirra.eCommerce.Service.Product.ProductService;
import com.Mirra.eCommerce.Service.User.Related.WalletService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@Controller
@RequestMapping("/admin/viewReturn")
public class ReturnControllerByAdmin {


    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private SerializeAndDeserialize serializeAndDeserialize;


    @Autowired
    private ReturnRequestService returnRequestService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WalletService walletService;


    @Autowired
    private ProductService productsService;

    @GetMapping("/returnPending/{orderId}/{returnStatus}")
    public String returnPendingForOrder(@PathVariable int orderId, @PathVariable String returnStatus, Model model) throws IOException, ClassNotFoundException {
        List<OrderItem> pendingOrderItems;
        ReturnStatus statusToFetch;



        if (returnStatus.equalsIgnoreCase("COMPLAINT")) {
            statusToFetch = ReturnStatus.COMPLAINT;
        } else {
            statusToFetch = ReturnStatus.PENDING;
        }

        pendingOrderItems = orderItemService.findByOrderIdAndReturnStatus(orderId, statusToFetch);
        model.addAttribute("pendingOrderItems", pendingOrderItems);

        List<ReturnRequest> returnRequests = new ArrayList<>();
        for (OrderItem pendingOrderItem : pendingOrderItems) {
            returnRequests.addAll(returnRequestService.getReturnRequestsByOrderItemId(Math.toIntExact(pendingOrderItem.getId())));
        }
        model.addAttribute("returnRequests", returnRequests);

        List<String> encodedImagesList = new ArrayList<>();
        for (OrderItem pendingOrderItem : pendingOrderItems) {
            List<byte[]> imageDataList = serializeAndDeserialize.deserializeImageBlob(pendingOrderItem.getProduct().getImageBlob());
            String encodedImage = Base64.getEncoder().encodeToString(imageDataList.get(0));
            encodedImagesList.add(encodedImage);
        }
        model.addAttribute("encodedImagesList", encodedImagesList);

        return "Admin/dashBoard/orders/ReturnApproval";
    }



//    if return approved

    @PostMapping("/approveReturn/{orderId}/{orderItemId}")
    @Transactional
    public String approveReturn(@PathVariable int orderId, @PathVariable int orderItemId, RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrderById(orderId);

        if (order == null || order.getStatus() != OrderStatus.DELIVERED) {
            return handleInvalidOrder(redirectAttributes);
        }

        OrderItem orderItem = orderService.getOrderItemById(orderItemId);

        if (orderItem == null || orderItem.getReturnStatus() != ReturnStatus.PENDING) {
            return handleInvalidOrderItem(redirectAttributes);
        }

        List<ReturnRequest> returnRequests = returnRequestService.getReturnRequestsByOrderItemId(orderItemId);

        if (returnRequests.isEmpty()) {
            return handleNoReturnRequests(redirectAttributes);
        }

        BigDecimal totalReturnedAmount = processReturnRequests(orderItem, returnRequests);

        updateUserWallet(order, totalReturnedAmount);

        if (allOrderItemsApproved(order)) {
            updateOrderStatusAndHistory(order);
        }

        redirectAttributes.addFlashAttribute("message", "Return approved.");
        return "redirect:/admin/ordersController";
    }

    private String handleInvalidOrder(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Returns are not allowed for this order.");
        return "redirect:/admin/ordersController";
    }

    private String handleInvalidOrderItem(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Invalid order item or return status.");
        return "redirect:/admin/ordersController";
    }

    private String handleNoReturnRequests(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "No return requests found.");
        return "redirect:/admin/myOrders";
    }

    private BigDecimal processReturnRequests(OrderItem orderItem, List<ReturnRequest> returnRequests) {
        BigDecimal totalReturnedAmount = BigDecimal.ZERO;

        for (ReturnRequest returnRequest : returnRequests) {
            Product product = orderItem.getProduct();
            int returnedQuantity = returnRequest.getReturnQuantity();
            int currentStock = product.getStock();

            if (orderItem.getReturnStatus() == ReturnStatus.PENDING) {
                product.setStock(currentStock + returnedQuantity);
                product.setActive(currentStock > 0);
                productsService.saveProduct(product);

                returnRequest.setStatus(ReturnStatus.APPROVED);
                orderItem.setReturnStatus(ReturnStatus.APPROVED);

                BigDecimal productActualPrice = orderItem.getPrice();
                BigDecimal returnedProductAmount = productActualPrice.multiply(BigDecimal.valueOf(returnedQuantity));
                totalReturnedAmount = totalReturnedAmount.add(returnedProductAmount);
            }
        }
        return totalReturnedAmount;
    }

    private void updateUserWallet(Order order, BigDecimal totalReturnedAmount) {
        Wallet existingWallet = walletService.findByUser(order.getUser());

        if (existingWallet == null) {
            Wallet newWallet = new Wallet();
            newWallet.setUser(order.getUser());
            newWallet.setAmount(totalReturnedAmount);
            walletService.save(newWallet);
        } else {
            BigDecimal currentAmount = existingWallet.getAmount();
            existingWallet.setAmount(currentAmount.add(totalReturnedAmount));
            walletService.save(existingWallet);
        }
    }

    private boolean allOrderItemsApproved(Order order) {
        return order.getOrderItems().stream()
                .allMatch(orderItemToCheck -> orderItemToCheck.getReturnStatus() == ReturnStatus.APPROVED);
    }

    private void updateOrderStatusAndHistory(Order order) {
        OrderStatusHistory statusHistory = new OrderStatusHistory();
        statusHistory.setOrder(order);
        statusHistory.setNewStatus(OrderStatus.RETURNED);
        statusHistory.setChangeTimestamp(LocalDateTime.now());

        order.setStatus(OrderStatus.RETURNED);

        if (order.getStatusHistory() == null) {
            order.setStatusHistory(new ArrayList<>());
        }
        order.getStatusHistory().add(statusHistory);

        orderService.saveOrder(order);
    }


//    if denied

    @PostMapping("/denyReturn/{orderId}/{orderItemId}")
    @Transactional
    public String denyReturn(@PathVariable int orderId, @PathVariable int orderItemId, RedirectAttributes redirectAttributes) {
        Order order = orderService.getOrderById(orderId);

        if (order == null || order.getStatus() != OrderStatus.DELIVERED) {
            return handleInvalidOrder(redirectAttributes);
        }

        OrderItem orderItem = orderService.getOrderItemById(orderItemId);

        if (orderItem == null || orderItem.getReturnStatus() != ReturnStatus.PENDING) {
            return handleInvalidOrderItem(redirectAttributes);
        }

        List<ReturnRequest> returnRequests = returnRequestService.getReturnRequestsByOrderItemId(orderItemId);

        if (returnRequests.isEmpty()) {
            return handleNoReturnRequests(redirectAttributes);
        }

        processReturnDEniedRequests(orderItem, returnRequests);


        if (allOrderItemsDenied(order)) {
            updateOrderStatusAndHistory(order);
        }

        redirectAttributes.addFlashAttribute("message", "Return denied.");
        return "redirect:/admin/ordersController";
    }


    private boolean allOrderItemsDenied(Order order) {
        return order.getOrderItems().stream()
                .allMatch(orderItemToCheck -> orderItemToCheck.getReturnStatus() == ReturnStatus.DENIED);
    }

    private void processReturnDEniedRequests(OrderItem orderItem, List<ReturnRequest> returnRequests) {
        for (ReturnRequest returnRequest : returnRequests) {
            if (orderItem.getReturnStatus() == ReturnStatus.PENDING) {
                returnRequest.setStatus(ReturnStatus.DENIED);
                orderItem.setReturnStatus(ReturnStatus.DENIED);

            }
        }
    }



}
