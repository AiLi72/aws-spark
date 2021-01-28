#!/bin/bash

sudo apt-get update -y
sudo hostnamectl set-hostname slave
sudo echo "slaveIP   slave" >> /etc/hosts
sudo echo "masterIP  master" >> /etc/hosts 
sudo swapoff -a 
sudo wget -qO- https://get.docker.com/ | sh 
sudo modprobe br_netfilter 
sudo cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1 
EOF
sudo sysctl --system
sudo curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add -
sudo cat <<EOF | sudo tee /etc/apt/sources.list.d/kubernetes.list
deb https://apt.kubernetes.io/ kubernetes-xenial main
EOF
sudo apt-get update 
sudo apt-get install -y kubelet kubeadm kubectl
