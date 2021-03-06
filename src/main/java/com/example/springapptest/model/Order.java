package com.example.springapptest.model;

import com.example.springapptest.common.BillStatus;
import com.example.springapptest.common.PaymentMethod;
import com.example.springapptest.common.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerAddress;

    private String customerMobile;

    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private BillStatus billStatus;

    private Status status=Status.Active;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime dateCreated;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<OderDetail> orderDetails=new HashSet<>();

    @Transient
    public void addOrderDetail(OderDetail orderDetail){
        if (orderDetail!=null){
            if (orderDetails==null){
                orderDetails=new HashSet<>();
            }
            orderDetails.add(orderDetail);
            orderDetail.setOrder(this);
        }
    }
}
