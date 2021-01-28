
package com.example.ec2;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupRequest;
import software.amazon.awssdk.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import software.amazon.awssdk.services.ec2.model.AuthorizeSecurityGroupIngressResponse;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;
import software.amazon.awssdk.services.ec2.model.IpPermission;
import software.amazon.awssdk.services.ec2.model.CreateSecurityGroupResponse;
import software.amazon.awssdk.services.ec2.model.IpRange;

public class CreateSecurityGroup {

    public static void main(String[] args) {
        String groupName = "security-groupAi";
        String groupDesc = "security group";
        String vpcId = "vpc-bb194ec3";

        Region region = Region.US_WEST_2;
        Ec2Client ec2 = Ec2Client.builder()
                .region(region)
                .build();

        String id = createEC2SecurityGroup(ec2, groupName, groupDesc, vpcId);
        System.out.printf(
                "Successfully created Security Group with this ID %s",
                id);
        ec2.close();
    }

    public static String createEC2SecurityGroup( Ec2Client ec2,String groupName, String groupDesc, String vpcId) {
        try {

            CreateSecurityGroupRequest createRequest = CreateSecurityGroupRequest.builder()
                    .groupName(groupName)
                    .description(groupDesc)
                    .vpcId(vpcId)
                    .build();

            CreateSecurityGroupResponse resp = ec2.createSecurityGroup(createRequest);

            IpRange ipRange = IpRange.builder()
                    .cidrIp("0.0.0.0/0").build();

            IpPermission ipPerm = IpPermission.builder()
                    .ipProtocol("-1")
                    .toPort(-1)
                    .fromPort(-1)
                    .ipRanges(ipRange)
                    .build();

            IpPermission ipPerm2 = IpPermission.builder()
                    .ipProtocol("-1")
                    .toPort(-1)
                    .fromPort(-1)
                    .ipRanges(ipRange)
                    .build();

            AuthorizeSecurityGroupIngressRequest authRequest =
                    AuthorizeSecurityGroupIngressRequest.builder()
                            .groupName(groupName)
                            .ipPermissions(ipPerm, ipPerm2)
                            .build();

            AuthorizeSecurityGroupIngressResponse authResponse =
                    ec2.authorizeSecurityGroupIngress(authRequest);

            System.out.printf(
                    "Successfully added ingress policy to Security Group %s",
                    groupName);

            return resp.groupId();

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return "";
    }
}
