package services

import (
	"fmt"

	"github.com/jinzhu/gorm"
)

const username = "test"
const password = "passw0rd"
const dbHost = "database"
const dbPort = "5432"
const dbName = "apidb"

var Db *gorm.DB

func OpenDatabase() {

	var err error

	fmt.Print(1)
	Db, err = gorm.Open("postgres", "postgres://"+username+":"+password+"@"+dbHost+":"+dbPort+"/"+dbName+"?sslmode=disable")
	if err != nil {
		panic("failed to connect database")
	}

}