<?php


include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$q3=mysql_query("Delete from Ip where IpId='ip2'") or Die("".mysql_error());

$q1=mysql_query("Insert into Ip values('ip2','".$_REQUEST['IpAddress1']."')") or Die("".mysql_error());

$q2=mysql_query("Insert into Ip values('ip2','".$_REQUEST['IpAddress2']."')") or Die("".mysql_error());

mysql_close() or Die("".mysql_error());

echo "Address uploaded";
?>