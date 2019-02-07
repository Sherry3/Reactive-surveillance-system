<?php


include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$q=mysql_query("SELECT IpId, Status FROM Device where DeviceId='".$_REQUEST['DeviceId']."'") or Die("".mysql_error());
$e=mysql_fetch_assoc($q);

$ipId=$e["IpId"];
$output[]=$e["Status"];

$q1=mysql_query("SELECT IpAddress FROM Ip where IpId='".$ipId."'") or Die("".mysql_error());
while($e1=mysql_fetch_array($q1))
{
    $output[]=$e1["IpAddress"];
}

print(json_encode($output));

mysql_close() or Die("".mysql_error());

?>