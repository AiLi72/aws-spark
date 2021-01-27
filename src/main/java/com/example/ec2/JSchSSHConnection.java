package com.example.ec2;

import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.Instance;


public class JSchSSHConnection {

    /**
     * Java SSH Connection Program
     */
    public static void main(String[] args) {
        String host="ec2-52-34-52-183.us-west-2.compute.amazonaws.com";
        String user="ubuntu";
        String command=
                "hostnamectl set-hostname master &&  " +
                "docker pull ubuntu && " +
                "docker rmi -f hello-world && " +
                "docker run -it --name mycontainer ubuntu /bin/bash && " +
                "exit && " +
                "docker ps -a";
        String privateKeyPath = "key-pair.pem";

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