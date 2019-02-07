<?php


include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$q=mysql_query("SELECT SystemId FROM SuperAdmin where UserId='".$_REQUEST['UserId']."'") or Die("".mysql_error());
$e=mysql_fetch_assoc($q);

$sysid=$e["SystemId"];

$q1=mysql_query("SELECT RegionId FROM Region where SystemId='".$sysid."'") or Die("".mysql_error());
while($e=mysql_fetch_array($q1))
{
    $o[]=$e["RegionId"];
}

$d="SELECT UserId FROM RegionMonitor where RegionId IN ('".implode("','",$o)."')";
$q2=mysql_query($d) or Die("".mysql_error());
while($e=mysql_fetch_assoc($q2))
{
    $o2[]=$e;
}

$d="SELECT Name, UserId FROM User where UserId IN ('".implode("','",$o2)."')";
$q2=mysql_query($d) or Die("".mysql_error());
while($e=mysql_fetch_assoc($q2))
{
    $output[]=$e;
}

print(json_encode($output));

mysql_close() or Die("".mysql_error());
?>