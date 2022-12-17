# sftp-server

Generate SSH Keys
So as you now understand how the authentication mechanism works, let’s go ahead and create our SSH Keys. For that, we need a special utility called ssh-keygen . If you are on windows, you can use Git Bash to generate the SSH Keys. You can create the SSH key by typing below command

ssh-keygen -t rsa

Once you type the above command, you will be asked for a location to store the key with the name id_rsa (this is our Private Key), along with this we also have a Public Key file with name id_rsa.pub (Refer below image). You can protect your private key with a passphrase to add an extra bit of security. But make sure to store the private key in a very secure place. Also, we will add the public key of the client to the remote server, usually stored in the file at location – ~/.ssh/authorized_keys

And then, create a file called authorized_keys ( I will create this file inside ‘src/main/resources’ folder for convenience, but do not store the file inside the source code if you want to use the server in productive manner). Now copy the contents of our public key file(id_rs.pub) and paste it inside the newly created authorized_key file. Our SFTP server lookups at the public key whenever it receives an authentication request from the client.

Original codes and explanation posts can be found at : https://github.com/SaiUpadhyayula/java-sftpserver
