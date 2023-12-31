package com.Mirra.eCommerce.Controller.AdminController.ReportController;



import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Models.Orders.OrderItem;
import com.Mirra.eCommerce.Service.Orders.OrderAdditionalService;
import com.Mirra.eCommerce.Service.Orders.OrderItemsAdditionalService;
import com.Mirra.eCommerce.Service.Orders.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.threeten.extra.YearWeek;
import org.thymeleaf.ITemplateEngine;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.*;

@Controller
@RequestMapping("admin/salesReport")
public class SalesReportController {


    @Autowired
    private OrderAdditionalService orderAdditionalService;


    @Autowired
    private OrderItemsAdditionalService orderItemsAdditionalService;

    @Autowired
    private ITemplateEngine templateEngine;

    @GetMapping("/delivered-orders")
    public String getDeliveredOrders(Model model,
                                     @RequestParam(name = "filterType", required = false) String filterType,
                                     @RequestParam(name = "filterDate", required = false) String filterDate,
                                     @RequestParam(name = "filterWeek", required = false) String filterWeek,
                                     @RequestParam(name = "filterMonth", required = false) String filterMonth,
                                     @RequestParam(name = "startDate", required = false) String startDate,
                                     @RequestParam(name = "endDate", required = false) String endDate,
                                     @RequestParam(name = "filterYear", required = false) Integer filterYear) {


        List<Order> deliveredOrders = getDeliveredOrdersByFilter(filterType, filterDate,startDate,endDate, filterWeek, filterMonth, filterYear,model);


        // Calculate the total purchase amount
        BigDecimal totalPurchase = calculateTotalPurchase(deliveredOrders);


        // Add the totalPurchase to the model
        model.addAttribute("totalPurchase", totalPurchase);


        model.addAttribute("orders", deliveredOrders);
        return "Admin/Report/sales-report"; // Thymeleaf view name
    }


    private List<Order> getDeliveredOrdersByFilter(
            String filterType, String filterDate, String startDate, String endDate,
            String filterWeek, String filterMonth, Integer filterYear, Model model) {
        List<Order> deliveredOrders = new ArrayList<>();

        System.out.println("filterDate: " + filterDate);
        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + endDate);

        // Repeating logic for adding selected filterType and filterDate to the model
        if (filterDate != null && !filterDate.trim().isEmpty()) {
            System.out.println("nana");
            filterType = "Per Date";
            try {
                LocalDate date = LocalDate.parse(filterDate);
                deliveredOrders = orderAdditionalService.getDeliveredOrdersByDay(date);
            } catch (DateTimeParseException e) {
                // Handle invalid date input
            }
        } else if (startDate != null && endDate != null && !startDate.trim().isEmpty() && !endDate.trim().isEmpty()) {
            filterType = "Days";
            System.out.println(startDate+" "+endDate+"Days");

            try {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = LocalDate.parse(endDate);

                if (start.isAfter(end)) {
                    // Handle the case where the start date is after the end date
                } else {
                    deliveredOrders = orderAdditionalService.getDeliveredOrdersByDateRange(start, end);
                    model.addAttribute("selectedStartDate", start);
                    model.addAttribute("selectedEndDate", end);
                }
            } catch (DateTimeParseException e) {
                // Handle invalid date input
            }
        } else if (filterWeek != null && !filterWeek.trim().isEmpty()) {
            filterType = "Per week";
            try {
                YearWeek yearWeek = YearWeek.parse(filterWeek);
                LocalDate weekStart = yearWeek.atDay(DayOfWeek.MONDAY);
                LocalDate weekEnd = yearWeek.atDay(DayOfWeek.SUNDAY).plusDays(1);
                deliveredOrders = orderAdditionalService.getDeliveredOrdersByWeek(weekStart, weekEnd);
            } catch (DateTimeParseException e) {
                // Handle invalid week input
            }
        } else if (filterMonth != null  && !filterMonth.trim().isEmpty()) {
            filterType = "Per Month";
            try {
                String[] yearMonthParts = filterMonth.split("-");
                if (yearMonthParts.length == 2) {
                    int year = Integer.parseInt(yearMonthParts[0]);
                    int month = Integer.parseInt(yearMonthParts[1]);
                    YearMonth yearMonth = YearMonth.of(year, month);
                    deliveredOrders = orderAdditionalService.getDeliveredOrdersByMonth(yearMonth);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                // Handle invalid month input
            }
        } else if (filterYear != null) {
            filterType = "Per Year";
            try {
                deliveredOrders = orderAdditionalService.getDeliveredOrdersByYear(filterYear);
            } catch (NumberFormatException e) {
                // Handle invalid year input
            }
        } else {
            deliveredOrders = orderAdditionalService.getDeliveredOrders();
        }

        // Common logic for adding selected filterType and filterDate to the model
        if (filterType != null) {
            model.addAttribute("selectedFilterType", filterType);
            if (filterType.equals("Per Date") || filterType.equals("Per week") || filterType.equals("Per Month")) {
                model.addAttribute("selectedFilterDate", filterDate != null ? filterDate : filterWeek != null ? filterWeek : filterMonth);
            } else if (filterType.equals("Days")) {
                model.addAttribute("selectedFilterDate", deliveredOrders.isEmpty() ? null : startDate + " to " + endDate);
            } else if (filterType.equals("Per Year")) {
                model.addAttribute("selectedFilterDate", filterYear);
            }
        }

        return deliveredOrders;
    }


    private BigDecimal calculateTotalPurchase(List<Order> orders) {
        BigDecimal totalPurchase = BigDecimal.ZERO;

        for (Order order : orders) {
            // Assuming there's a method in your 'Order' class to get the purchase total
            BigDecimal purchaseTotal = order.getPurchaseTotal();

            // Add the purchase total to the running total
            totalPurchase = totalPurchase.add(purchaseTotal);
        }

        return totalPurchase;
    }

    @GetMapping("/perMonth/delivered-orders")
    public String getDeliveredOrdersPerMonth(Model model,

                                     @RequestParam(name = "filterMonth", required = false) String filterMonth) {


        List<Order> deliveredOrders = getDeliveredOrdersByFilterMonth(filterMonth);
        model.addAttribute("orders", deliveredOrders);

        // Calculate the total purchase amount
        BigDecimal totalPurchase = calculateTotalPurchase(deliveredOrders);
        // Add the totalPurchase to the model
        model.addAttribute("totalPurchase", totalPurchase);
        System.out.println("totalsPurchasePer Month "+totalPurchase);



        List<OrderItem> refundedOrders = getDeliveredOrdersAndNotReturnedByFilterMonth(filterMonth);

        model.addAttribute("refundedOrders", refundedOrders);


        BigDecimal refundedAmount= calculateRefundGiven(refundedOrders);
        // Add the totalPurchase to the model
        model.addAttribute("refundedAmount", refundedAmount);
        System.out.println("refundedAmount"+refundedAmount);


        BigDecimal profit=totalPurchase.subtract(refundedAmount);
        model.addAttribute("profit",profit);

        System.out.println("prfit of month "+profit);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);



        List<Order> deliveredOrdersPerYear = getDeliveredOrdersByFilterYear(currentYear);
        model.addAttribute("ordersPerYear", deliveredOrdersPerYear);

        // Calculate the total purchase amount
        BigDecimal totalPurchasePerYear = calculateTotalPurchase(deliveredOrdersPerYear);
        // Add the totalPurchase to the model
        model.addAttribute("totalPurchasePerYear", totalPurchasePerYear);
        System.out.println("totalPurchasePerYear"+totalPurchasePerYear);

        List<OrderItem> refundedOrdersPerYear = getDeliveredOrdersAndNotReturnedByFilterYear(currentYear);

        model.addAttribute("refundedOrdersPerYear", refundedOrdersPerYear);


        BigDecimal refundedAmountPerYear= calculateRefundGiven(refundedOrdersPerYear);
        // Add the totalPurchase to the model
        model.addAttribute("refundedAmountPerYear", refundedAmountPerYear);
        System.out.println("refundedAmountPerYear"+refundedAmountPerYear);


        BigDecimal profitPerYear=totalPurchasePerYear.subtract(refundedAmountPerYear);
        model.addAttribute("profitPerYear",profitPerYear);
        System.out.println("prfit of year "+profitPerYear);


        return "fragments/adminBasic"; // Thymeleaf view name
    }




    private List<Order> getDeliveredOrdersByFilterMonth(
           String filterMonth) {
        List<Order> deliveredOrders = new ArrayList<>();

        if(filterMonth != null  && !filterMonth.trim().isEmpty()) {
            try {
                String[] yearMonthParts = filterMonth.split("-");
                if (yearMonthParts.length == 2) {
                    int year = Integer.parseInt(yearMonthParts[0]);
                    int month = Integer.parseInt(yearMonthParts[1]);
                    YearMonth yearMonth = YearMonth.of(year, month);
                    deliveredOrders = orderAdditionalService.getDeliveredOrdersByMonth(yearMonth);
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                // Handle invalid month input
            }
        }
        return deliveredOrders;
    }


    private List<OrderItem> getDeliveredOrdersAndNotReturnedByFilterMonth(
            String filterMonth) {
        List<OrderItem> deliveredOrders = new ArrayList<>();

        if(filterMonth != null  && !filterMonth.trim().isEmpty()) {
            try {
                String[] yearMonthParts = filterMonth.split("-");
                if (yearMonthParts.length == 2) {
                    int year = Integer.parseInt(yearMonthParts[0]);
                    int month = Integer.parseInt(yearMonthParts[1]);
                    YearMonth yearMonth = YearMonth.of(year, month);
                    deliveredOrders = orderItemsAdditionalService.getDeliveredOrdersByMonth(yearMonth);

                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                // Handle invalid month input
            }
        }
        return deliveredOrders;
    }


    private BigDecimal calculateRefundGiven(List<OrderItem> orders) {
        BigDecimal totalPurchase = BigDecimal.ZERO;

        for (OrderItem order : orders) {
            System.out.println("on text"+order.getId());

            // Assuming there's a method in your 'Order' class to get the purchase total
            BigDecimal purchaseTotal = order.getPrice();
            // Add the purchase total to the running total
            totalPurchase = totalPurchase.add(purchaseTotal);
            System.out.println("test totalPurchase"+totalPurchase);
        }

        return totalPurchase;
    }


    private List<Order> getDeliveredOrdersByFilterYear(
            int filterYear) {
        List<Order> deliveredOrders = new ArrayList<>();

        if(filterYear != 0) {
            try {
                deliveredOrders = orderAdditionalService.getDeliveredOrdersByYear(filterYear);

            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                // Handle invalid month input
            }
        }
        return deliveredOrders;
    }


    private List<OrderItem> getDeliveredOrdersAndNotReturnedByFilterYear(
            int filterYear) {
        List<OrderItem> deliveredOrders = new ArrayList<>();

        if(filterYear != 0) {
            try {
                    deliveredOrders = orderItemsAdditionalService.getDeliveredOrdersByYear(filterYear);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                // Handle invalid month input
            }
        }
        return deliveredOrders;
    }



}


