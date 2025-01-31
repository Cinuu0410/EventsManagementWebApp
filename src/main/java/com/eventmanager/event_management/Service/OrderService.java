package com.eventmanager.event_management.Service;

import com.eventmanager.event_management.Enum.OrderStatus;
import com.eventmanager.event_management.Model.Event;
import com.eventmanager.event_management.Model.Order;
import com.eventmanager.event_management.Model.User;
import com.eventmanager.event_management.Repository.EventRepository;
import com.eventmanager.event_management.Repository.OrderRepository;
import com.eventmanager.event_management.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    public List<Order> getOrdersByUserId(Long userId){
        return orderRepository.findByUserId(userId);
    }

    public void createOrder(Long userId, Long eventId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Wydarzenie nie znalezione"));

        Order order = new Order();
        order.setUser(user);
        order.setEvent(event);
        order.setTicketQuantity(quantity);
        order.setStatus(OrderStatus.Zlozone);

        orderRepository.save(order);
    }

    public List<Order> getOrdersByCategory(String category) {
        return orderRepository.findOrdersByEventCategory(category);
    }


    public void changeOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono zamówienia o ID: " + orderId));
        order.setStatus(status);
        orderRepository.save(order);
    }
}
