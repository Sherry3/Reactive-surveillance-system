<?php

include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$q=mysql_query("SELECT  ZoneId FROM ZoneMonitor where UserId='".$_REQUEST['UserId']."'") or Die("".mysql_error());

while($e=mysql_fetch_assoc($q))
{
    $output[]=$e["ZoneId"];
}

$q=mysql_query("SELECT  ZoneId, Zone_Name FROM Zone where ZoneId='".implode("','",$output)."'") or Die("".mysql_error());

while($e=mysql_fetch_assoc($q))
{
    $o[]=$e;
}

print(json_encode($o));

mysql_close() or Die("".mysql_error());
?>