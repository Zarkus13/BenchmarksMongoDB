package fr.talium.mongodb.test

import com.mongodb.casbah.Imports._
import com.mongodb.{Mongo, DB, DBRef}
import org.squeryl.{Session, SessionFactory}
import org.squeryl.adapters.MySQLAdapter
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.Schema

/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 12/06/13
 * Time: 10:00
 * To change this template use File | Settings | File Templates.
 */
object Main {

    def main(args: Array[String]) {
        Class.forName("com.mysql.jdbc.Driver")
        SessionFactory.concreteFactory = Some(() => Session.create(
            java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/benchmarks", "java", "java"),
            new MySQLAdapter)
        )

//        transaction {
//            models.DB.create
//        }
//
//        BenchmarkMySQL.insertCategories(10000)
//        BenchmarkMySQL.retrieveCategories()
//        BenchmarkMySQL.insertProducts(200000)
//
//        BenchmarkMongoDB.insertCategories(10000)
//        BenchmarkMongoDB.retrieveCategories()
//        BenchmarkMongoDB.insertProducts(200000)
        BenchmarkMongoDB.retrieveProductsByCategory("50")
    }
}
