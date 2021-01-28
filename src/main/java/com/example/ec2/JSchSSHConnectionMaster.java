package com.example.ec2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Instance;


public class JSchSSHConnectionMaster {

    /**
     * Java SSH Connection Program
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        String host="ec2-54-200-111-154.us-west-2.compute.amazonaws.com";
        String user="ubuntu";
        String command= "sudo bash master_conf.sh";

        String privateKeyPath = "key-pair.pem";

        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("scp -o StrictHostKeyChecking=no -i " + privateKeyPath + " master_conf.sh "+user+"@"+host+":/home/ubuntu");
        process.waitFor();
        BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line =buf.readLine()) != null) {
            System.out.println(line);
        }

        try{
            JSch jsch = new JSch();
            jsch.addIdentity(privateKeyPath);
            Session session=jsch.getSession(user, host, 22);

            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
            System.out.println("Connected");

            Channel channel=session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec)channel).setErrStream(System.err);

            InputStream in=channel.getInputStream();
            channel.connect();
            byte[] tmp=new byte[1024];
            while(true){
                while(in.available()>0){
                    int i=in.read(tmp, 0, 1024);
                    if(i<0)break;
                    System.out.print(new String(tmp, 0, i));
                }
                if(channel.isClosed()){
                    System.out.println("exit-status: "+channel.getExitStatus());
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){}
            }
            channel.disconnect();
            session.disconnect();
            System.out.println("DONE");
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}