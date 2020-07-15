fun Fridge.take(productName: String): Product {
    this.open()
    val product = this.find(productName)
    this.close()
    return product
}