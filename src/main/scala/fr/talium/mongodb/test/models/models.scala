/**
 * Created with IntelliJ IDEA.
 * User: alexis
 * Date: 19/03/13
 * Time: 13:54
 * To change this template use File | Settings | File Templates.
 */
package fr.talium.mongodb.test.models

import org.squeryl.{Schema, Table, KeyedEntity}
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl.{CompositeKey2, CompositeKey}


trait Model extends KeyedEntity[Long] {
    type T <: Model
    def table: Table[T]
    val id: Long = 0

    def save(): Unit = {
        inTransaction {
            table.insertOrUpdate(this.asInstanceOf[T])
        }
    }

    def delete(): Unit = {
        inTransaction {
            table.delete(id)
        }
    }
}

trait StaticModel {
    type T <: Model
    def table: Table[T]

    def all: List[T] = {
        inTransaction {
            from(table)(i => select(i)).toList
        }
    }

    def findById(id: Long): Option[T] = {
        inTransaction {
            table.lookup(id)
        }
    }

    def deleteById(id: Long): Boolean = {
        inTransaction {
            table.delete(id)
        }
    }
}

class ProductsToCategories(val productId: Long, val categoryId: String) extends KeyedEntity[CompositeKey2[Long, String]] {
    def id = compositeKey(productId, categoryId)
}

object DB extends Schema {
    val categories = table[Category]("categories")
    val products = table[Product]("products")

    // RELATIONS
    val categoriesToSubCategories = oneToManyRelation(categories, categories)
        .via((c, sc) => c.id === sc.parentId.getOrElse(null))

    val productsToCategories =
        manyToManyRelation(products, categories)
        .via[ProductsToCategories]((p, c, pc) => (pc.productId === p.id, c.id === pc.categoryId))
}