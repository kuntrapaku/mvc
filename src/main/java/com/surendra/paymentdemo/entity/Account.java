package com.surendra.paymentdemo.entity; // this class belongs to entity package

import jakarta.persistence.Entity; // tells hibernate to save this class into db
import jakarta.persistence.Id; // marks primary key
import jakarta.persistence.Table; // gives db table name
import jakarta.persistence.Version; // avoids wrong updates from multiple users
import java.math.BigDecimal; // money should use BigDecimal
import java.util.Objects; // used in equals() and hashCode()
import com.surendra.paymentdemo.exception.InsufficientBalanceException; // custom balance exception

@Entity // account object should become db row
@Table(name = "accounts") // db table name
public class Account {

    @Id // unique id
    private Long id; // account id

    private String customerName; // account holder name

    private BigDecimal balance; // account balance

    @Version // optimistic locking
    private Long version;

    protected Account() { } // hibernate creates empty object first

    public Account(Long id, String customerName, BigDecimal balance) { // constructor creates valid account
        this.id = id; // assign id
        this.customerName = customerName; // assign name
        this.balance = balance; // assign balance
    }

    public void debit(BigDecimal amount) { // money leaving account

        if (this.balance.compareTo(amount) < 0) { // stop if balance is less
            throw new InsufficientBalanceException("insufficient balance"); // clean business exception
        }

        this.balance = this.balance.subtract(amount); // reduce balance
    }

    public void credit(BigDecimal amount) { // money entering account
        this.balance = this.balance.add(amount); // increase balance
    }

    public Long getId() { // return id
        return id;
    }

    public String getCustomerName() { // return name
        return customerName;
    }

    public BigDecimal getBalance() { // return balance
        return balance;
    }

    @Override // compare account ids instead of memory
    public boolean equals(Object o) { // java sends any object because Object is parent

        if (this == o) return true; // same memory object

        if (!(o instanceof Account other)) return false; // if not Account, return false

        return Objects.equals(this.id, other.id); // compare ids
    }

    @Override // hash should also use id
    public int hashCode() {
        return Objects.hash(this.id);
    }
}