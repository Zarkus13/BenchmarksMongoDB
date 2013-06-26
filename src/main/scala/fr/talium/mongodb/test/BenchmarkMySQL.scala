package fr.talium.mongodb.test

import scala.collection.mutable
import fr.talium.mongodb.test.models.{Product, DB, Category}
import org.squeryl.PrimitiveTypeMode._
import scala.util.Random
import scala.compat.Platform

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 13/06/13
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */
object BenchmarkMySQL {

    val categories: mutable.MutableList[Category] = new mutable.MutableList[Category]
    var categoriesList: List[Category] = List()

    val products: mutable.MutableList[Product] = new mutable.MutableList[Product]
    var productsList: List[Product] = List()

    def insertCategories(number: Int) {
        transaction {
            DB.categories.delete(from(DB.categories)(c => select(c)))
        }

        println("\n\nCreating categories ...")
        for(i <- 1 to number) {
            categories += createCategory(String.valueOf(i))
        }
        println("Done creating categories !")

        categoriesList = categories.toList

        println("Inserting categories into MySQL ...")
        val t1 = Platform.currentTime
        transaction {
            DB.categories.insert(categoriesList)
        }
        val t2 = Platform.currentTime

        println("Time for inserting " + categoriesList.size + " categories in MySQL : " + (t2 - t1) + " ms")
    }

    def createCategory(id: String, parentId: Option[String] = None): Category = {
        val c: Category = Category(
            id,
            ("Category " + id),
            parentId
        )

        if(Random.nextBoolean() && id.count(c => c == '.') < 3) {
            for(i <- 1 to Random.nextInt(10)) {
                categories += createCategory((id + "." + i), Some(id))
            }
        }

        c
    }

    def retrieveCategories() {
        transaction {
            val t1 = Platform.currentTime
            categoriesList = (
                from(DB.categories)(c => select(c))
            ).toList
            val t2 = Platform.currentTime

            println("Time for retrieving " + categoriesList.size + " categories from MySQL : " + (t2 - t1) + " ms")
        }
    }

    def insertProducts(number: Int) {
        transaction {
            DB.products.delete(from(DB.products)(p => select(p)))

            println("Creating products ...")
            for(i <- 1 to number) {
                println("Product " + i)
                val p: Product = Product(
                    i,
                    "Product " + i
                )

                if(Random.nextBoolean()) {
                    p.categories.associate(categoriesList(30))
                    p.categories.associate(categoriesList(100))
                }
                else {
                    p.categories.associate(categoriesList(50))
                    p.categories.associate(categoriesList(200))
                }

                products += p
            }
            println("Products created !")

            productsList = products.toList

            println("Inserting products in database ...")
            val t1 = Platform.currentTime
            DB.products.insert(productsList)
            val t2 = Platform.currentTime

            println("Time for inserting " + productsList.size + " products in MySQL : " + (t2 - t1) + " ms")
        }
    }
}
