<?php

include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());


$q=mysql_query("SELECT ZoneId FROM Zone where RegionId='".$_REQUEST['RegionId']."'") or Die("".mysql_error());

while($e=mysql_fetch_assoc($q))
{    
    $q1=mysql_query("DELETE FROM Zone WHERE ZoneId='".$e['ZoneId']."'") or Die("".mysql_error());

    $q2=mysql_query("DELETE FROM ZoneMonitor where ZoneId='".$e['ZoneId']."'") or Die("".mysql_error());
}       

$q1=mysql_query("DELETE FROM Region WHERE RegionId='".$_REQUEST['RegionId']."'") or Die("".mysql_error());

$q2=mysql_query("DELETE FROM RegionMonitor where RegionId='".$_REQUEST['RegionId']."'") or Die("".mysql_error());

echo "Deleted";

mysql_close() or Die("".mysql_error());
?>