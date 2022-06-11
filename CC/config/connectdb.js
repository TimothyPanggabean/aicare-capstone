const { Client } = require('pg')
require('dotenv').config()

const client = new Client({
    host: process.env.DB_HOST,
    port: process.env.DB_PORT,
    user: process.env.DB_USER,
    database: process.env.DB_NAME,
    password: process.env.DB_PASS
})

const connectDB = () => {
    client.connect((err) => {
        if(err) {
            console.log(err)
            return
        }
        console.log('Database successfully connected!')
    })
}

module.exports = { connectDB, client }
