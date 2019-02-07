<?php


include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$q=mysql_query("SELECT  RegionId FROM RegionMonitor where UserId='".$_REQUEST['UserId']."'") or Die("".mysql_error());
$e=mysql_fetch_assoc($q);

$regid=$e["RegionId"];

$q1=mysql_query("SELECT  ZoneId, Zone_Name  FROM Zone where RegionId='".$regid."'") or Die("".mysql_error());
while($e=mysql_fetch_assoc($q1))
{
    $output[]=$e;
}

print(json_encode($output));

mysql_close() or Die("".mysql_error());
?>