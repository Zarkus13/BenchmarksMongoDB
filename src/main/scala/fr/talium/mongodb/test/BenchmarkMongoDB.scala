package fr.talium.mongodb.test

import com.mongodb.casbah.MongoClient
import scala.util.Random
import com.mongodb.casbah.commons.MongoDBObject
import scala.compat.Platform
import com.mongodb.DBObject
import scala.collection.mutable
import com.mongodb.casbah.Imports._

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 13/06/13
 * Time: 10:14
 * To change this template use File | Settings | File Templates.
 */
object BenchmarkMongoDB {

    val categoriesClient = MongoClient()("test")("categories")
    val productsClient = MongoClient()("test")("products")

    val categories: mutable.MutableList[DBObject] = new mutable.MutableList[DBObject]
    var categoriesList: List[DBObject] = List()
    val products: mutable.MutableList[DBObject] = new mutable.MutableList[DBObject]

    def insertCategories(number: Int) {
        categoriesClient.drop()

        println("\n\nCreating categories ...")
        for(i <- 1 to number) {
            categories += createCategory(String.valueOf(i))
        }
        println("Done creating categories !")

        categoriesList = categories.toList

        println("Inserting categories into MongoDB ...")
        val t1 = Platform.currentTime
        categoriesClient.insert(categoriesList:_*)
        val t2 = Platform.currentTime

        println("Time for inserting " + categoriesList.size + " categories in MongoDB : " + (t2 - t1) + " ms")
    }

    def createCategory(id: String, parentId: String = null): DBObject = {
        val builder = MongoDBObject.newBuilder
        builder += "_id" -> id
        builder += "name" -> ("Category " + id)
        if(parentId != null)
            builder += "parent" -> parentId

        if(Random.nextBoolean() && id.count(c => c == '.') < 3) {
            for(i <- 1 to Random.nextInt(10)) {
                categories += createCategory((id + "." + i), id)
            }
        }

        builder.result()
    }

    def retrieveCategories() {
        val t1 = Platform.currentTime
        val cats = categoriesClient.find()
        println(cats.size)
        val t2 = Platform.currentTime

        println("Time for retrieving " + cats.size + " categories from MongoDB : " + (t2 - t1) + " ms")

        //        cats.foreach(c => println(c.get("name")))
    }

    def insertProducts(number: Int) {
        productsClient.drop()

        println("\n\nCreating products ...")
        for(i <- 1 to number) {
            val builder = MongoDBObject.newBuilder
            builder += "_id" -> i
            builder += "name" -> ("Product " + i)

            builder += "categories" ->
                (if(Random.nextBoolean()) List("30", "100") else List("50", "200"))
//            (
//                for(j <- 1 to Random.nextInt(10))
//                yield categoriesList(Random.nextInt(categories.size)).get("_id")
//            )
//            builder += "test" -> Random.nextInt(100)

            products += builder.result()
        }
        println("Done creating products !")

        println("Inserting products into MongoDB ...")
        val t1 = Platform.currentTime
        productsClient.insert(products:_*)
        val t2 = Platform.currentTime

        println("Time for inserting " + products.size + " products in MongoDB : " + (t2 - t1) + " ms")
    }

    def retrieveProductsByCategory(category: String) {
        val t1 = Platform.currentTime
        val prods = productsClient.find(MongoDBObject("categories" -> category))
        val t2 = Platform.currentTime

//        prods.foreach(println)

        println("\nTime for retrieving " + prods.size + " products by category " + category + " from MongoDB : " + (t2 - t1) + " ms")
    }
}
