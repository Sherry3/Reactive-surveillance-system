<?php

function addUser($c)
{
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$q1=mysql_query("SELECT UserId FROM User") or Die("".mysql_error());

$max=0;
    
while($e1=mysql_fetch_assoc($q1))
{
    $temp=intval(str_replace("usr","",$e1["UserId"]));
    if($temp>$max)
        $max=$temp;
}

$max++;

$UserId="usr".$max;

$query="Insert into User values('$UserId','null','".$_REQUEST['UserName']."','".$_REQUEST['Phone']."','".$_REQUEST['Address']."','".$_REQUEST['Password']."','".$_REQUEST['Rights']."','".$_REQUEST['Email']."',NULL);";

$result=mysql_query($query)
or Die(mysql_error());

mysql_close() or Die("".mysql_error());

echo $UserId;

return $UserId;    
}

?>