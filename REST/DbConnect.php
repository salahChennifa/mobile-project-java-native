<?php
$servername = "localhost";
$database   = "id16713991_project";
$username   = "id16713991_root";
//my personnel data base
$password   = "????????????????????";

$conn = new mysqli($servername, $username, $password, $database);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
?>
