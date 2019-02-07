<?php

include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());


$q=mysql_query("SELECT IpId FROM Device where DeviceId='".$_REQUEST['DeviceId']."'") or Die("".mysql_error());
$e=mysql_fetch_assoc($q);

$ipId=$e["IpId"];

$q1=mysql_query("DELETE FROM Device WHERE DeviceId='".$_REQUEST['DeviceId']."'") or Die("".mysql_error());

$q2=mysql_query("DELETE FROM Ip where IpId='".$ipId."'") or Die("".mysql_error());

echo "Deleted";

mysql_close() or Die("".mysql_error());
?>