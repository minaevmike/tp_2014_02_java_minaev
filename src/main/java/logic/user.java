package logic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="users")
public class user {
    private Long id;
    private String name;
    private String pass;

    public user(){
        name = null;
    }

    public user(user u){
        name = u.getName();
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name="id")
    public Long getId(){
        return id;
    }

    @Column(name = "username", unique = true)
    public String getName() {
        return name;
    }

    @Column(name = "password")
    public String getPass(){
        return pass;
    }

    public void setId(Long i){
        id = i;
    }

    public void setName(String n){
        name = n;
    }

    public void setPass(String p){
        pass = p;
    }
}
