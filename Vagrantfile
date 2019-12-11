Vagrant.configure("2") do |config|
  config.vm.box = "bento/ubuntu-14.04"
  config.vm.provider :virtualbox do |v|
    v.customize ["modifyvm", :id, "--memory",8]
    v.customize ["modifyvm", :id, "--cpus", 8]
  end
end