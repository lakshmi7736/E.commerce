package com.Mirra.eCommerce.Controller.AdminController.OrderController;

import com.Mirra.eCommerce.Models.Orders.Order;
import com.Mirra.eCommerce.Service.Orders.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;


@Controller
@RequestMapping("/user/orders/invoice")
public class InvoiceController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{orderId}")
    public String invoice(@PathVariable int orderId, Model model) throws IOException, ClassNotFoundException {

        Order order = orderService.getOrderById(orderId);

        if (order != null) {
            model.addAttribute("order", order);

            return "Admin/Order/Invoice"; // Create a Thymeleaf HTML template for invoice orders
        } else {
            return "redirect:/";
        }
    }

//    @GetMapping("/{orderId}/pdf")
//    public void generatePdfInvoice(@PathVariable int orderId, HttpServletResponse response) throws IOException, DocumentException {
//        // Fetch the order (Replace with your order retrieval logic)
//        Order order = orderService.getOrderById(orderId);
//
//        // Create a Thymeleaf context and add the order
//        Context context = new Context();
//        context.setVariable("order", order);
//
//        // Process the Thymeleaf template
//        String thymeleafTemplate = "invoice"; // Update with your actual template name
//        String htmlContent = templateEngine.process(thymeleafTemplate, context);
//
//        // Load the CSS content from the external CSS file
//        String cssContent = "";
//        try {
//            ClassPathResource cssResource = new ClassPathResource("static/frontEnd/css/styles.min.css");
//            cssContent = new String(cssResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            // Handle or log the exception
//        }
//
//        // Insert the CSS into the HTML content
//        htmlContent = htmlContent.replace("</head>", "<style>" + cssContent + "</style></head>");
//
//        // Generate the PDF
//        ITextRenderer renderer = new ITextRenderer();
//        renderer.setDocumentFromString(htmlContent);
//        renderer.layout();
//
//        // Set the response content type and headers
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "attachment; filename=invoice.pdf");
//
//        // Generate the PDF to the response stream
//        try (OutputStream os = response.getOutputStream()) {
//            renderer.createPDF(os);
//        } catch (IOException | DocumentException e) {
//            // Handle or log the exception
//        }
//    }
//


}
