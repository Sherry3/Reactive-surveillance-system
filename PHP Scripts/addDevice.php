<?php


include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$q2=mysql_query("SELECT DeviceId FROM Device") or Die("".mysql_error());

$max=0;
    
while($e2=mysql_fetch_assoc($q2))
{
    $temp=intval(str_replace("dv","",$e2["DeviceId"]));
    if($temp>$max)
        $max=$temp;
}

$max++;

$DeviceId="dv".$max;


$q2=mysql_query("SELECT IpId FROM Ip") or Die("".mysql_error());

$max=0;
    
while($e2=mysql_fetch_assoc($q2))
{
    $temp=intval(str_replace("ip","",$e2["IpId"]));
    if($temp>$max)
        $max=$temp;
}

$max++;

$IpId="ip".$max;

$query="Insert into Ip values('$IpId','0.0.0.0');";

$result=mysql_query($query)
or Die(mysql_error());

$query="Insert into Device values('$DeviceId','".$_REQUEST['ZoneId']."','"
                                                .$_REQUEST['Type']."', '"
                                                .$_REQUEST['Status']."','$IpId');";


$result=mysql_query($query)
or Die(mysql_error());

echo $DeviceId;

mysql_close() or Die("".mysql_error());
?>