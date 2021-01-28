package com.example.ec2;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        CreateKeyPair.main(null);
        CreateSecurityGroup.main(null);
        CreateInstanceMaster.main(null);
        CreateInstanceSlave.main(null);
        JSchSSHConnectionMaster.main(null);
        JSchSSHConnectionSlave.main(null);
    }

}
