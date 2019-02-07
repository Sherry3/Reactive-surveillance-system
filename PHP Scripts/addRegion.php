<?php


include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$q1=mysql_query("SELECT SystemId FROM SuperAdmin where UserId='".$_REQUEST['UserId']."'") or Die("".mysql_error());
$e1=mysql_fetch_assoc($q1);
$sysid=$e1["SystemId"];

$q2=mysql_query("SELECT RegionId FROM Region") or Die("".mysql_error());

$max=0;
    
while($e2=mysql_fetch_assoc($q2))
{
    $temp=intval(str_replace("reg","",$e2["RegionId"]));
    if($temp>$max)
        $max=$temp;
}

$max++;

$RegionId="reg".$max;

$query="Insert into Region values('$RegionId','$sysid','".$_REQUEST['Region_Name']."',NULL);";

$result=mysql_query($query)
or Die(mysql_error());

echo $RegionId;

mysql_close() or Die("".mysql_error());
?>