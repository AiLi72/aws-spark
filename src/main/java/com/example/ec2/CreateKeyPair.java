package com.example.ec2;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateKeyPairRequest;
import software.amazon.awssdk.services.ec2.model.CreateKeyPairResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;

public class CreateKeyPair {
    public static void main(String[] args) {

        String keyName = "KeyPair";
        Region region = Region.US_WEST_2;
        Ec2Client ec2 = Ec2Client.builder()
                .region(region)
                .build();

        createEC2KeyPair(ec2, keyName) ;
        ec2.close();
    }

    public static void createEC2KeyPair(Ec2Client ec2,String keyName ) {

        try {
            CreateKeyPairRequest request = CreateKeyPairRequest.builder()
                    .keyName(keyName).build();

            CreateKeyPairResponse response = ec2.createKeyPair(request);
            System.out.printf(
                    "Successfully created key pair named %s",
                    keyName);
            //save the keyPair ??

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}
