<?php

include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$q=mysql_query("SELECT * FROM User where UserId='".$_REQUEST['UserId']."'") or Die("".mysql_error());
$temp="";

while($e=mysql_fetch_assoc($q))
{
    $output[]=$e["Name"];
    $output[]=$e["UserId"];
    $output[]=$e["Address"];
    $output[]=$e["Phone"];
    $output[]=$e["Email"];
    $temp=$e["Rights"];
    $output[]=$temp;
}

if($temp=="0")
{
    $q=mysql_query("SELECT SystemId FROM SuperAdmin where UserId='".$_REQUEST['UserId']."'") or Die("".mysql_error());
    $e=mysql_fetch_assoc($q);

    $output[]=$e["SystemId"];
}
else if($temp=="1")
{
    $q=mysql_query("SELECT RegionId FROM RegionMonitor where UserId='".$_REQUEST['UserId']."'") or  Die("".mysql_error());

    while($e=mysql_fetch_assoc($q))
    {
        $output[]=$e["RegionId"];
    }
}
else if($temp=="2")
{
    $q=mysql_query("SELECT ZoneId FROM ZoneMonitor where UserId='".$_REQUEST['UserId']."'") or  Die("".mysql_error());

    while($e=mysql_fetch_assoc($q))
    {
        $output[]=$e["ZoneId"];
    }
}

echo implode(",",$output);

mysql_close() or Die("".mysql_error());
?>