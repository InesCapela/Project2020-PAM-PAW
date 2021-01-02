package routes

import (
	"api/controllers"

	"github.com/gin-gonic/gin"
)

// CreateInvoice function
func CreateInvoice(c *gin.Context) {
	controllers.CreateInvoice(c)
}

// GetUserInvoices function
func GetUserInvoices(c *gin.Context) {
	controllers.GetUserInvoices(c)
}

// GetInvoiceByID function
func GetInvoiceByID(c *gin.Context) {
	controllers.GetInvoiceByID(c)
}

// UpdateInvoiceByID function
func UpdateInvoiceByID(c *gin.Context) {
	controllers.UpdateInvoiceByID(c)
}

// DeleteInvoiceByID function
func DeleteInvoiceByID(c *gin.Context) {
	controllers.DeleteInvoiceByID(c)
}
