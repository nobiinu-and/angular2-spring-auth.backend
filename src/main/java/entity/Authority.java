/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author and
 */
public class Authority implements GrantedAuthority {
    private String name;

    public Authority() {
    }
    
    public Authority(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
    
    
}
