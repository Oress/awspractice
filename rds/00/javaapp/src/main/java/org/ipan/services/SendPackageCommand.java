package org.ipan.services;

import java.math.BigDecimal;

import org.ipan.model.PackageSize;

public class SendPackageCommand {
    public String senderEmail;
    public String receiverEmail;
    public PackageSize packageSize;
    public BigDecimal packgeWeight;
}
