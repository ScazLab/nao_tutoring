## Annie and Kendrick's instructions for setting up the Nao

Network = dfs2, password = beefylounge

plug in ethernet and power for nao
connect tablet to dfs2

Nao's IP address, last three numbers can change
192.168.42.119, check to make sure it's right in ip.txt

check ip address of computer:
```
$ ifconfig
```
then the wlan0, inet addr,

run the script
```
python nao_server.py [ip address of computer] [port] -robot
```
to start tutoring interaction, type s, robot will go into official sitting 

now, on tablet
computer'sIP:port

choose fixed breaks, break interval = 1

username and password when the ip address is entered into a web browser
nao,nao

Shared username for git pushing:  nao
