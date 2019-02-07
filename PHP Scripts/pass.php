<?php


include('credentials.php');
$c=new me;
    
mysql_connect($c->host,$c->username,$c->password) or Die("".mysql_error());
mysql_select_db($c->database) or Die("".mysql_error());

$query="Update User set Password=md5(utf8_encode('".$_REQUEST['Password']."'));";

$result=mysql_query($query)

mysql_close() or Die("".mysql_error());
?>