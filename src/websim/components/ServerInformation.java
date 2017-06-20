/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websim.components;

import java.math.BigDecimal;

/**
 *
 * @author eitz
 */
public class ServerInformation {
    public String name;
    public BigDecimal processorUsage;

    public ServerInformation(String name, BigDecimal processorUsage) {
        this.name = name;
        this.processorUsage = processorUsage;
    }
}
