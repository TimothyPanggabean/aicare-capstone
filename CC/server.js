const express = require('express')
require('dotenv').config()
const hospitalRoutes = require('./routes/hospitalRoutes')
const userRoutes = require('./routes/userRoutes')
const historyRoutes = require('./routes/historyRoutes')
const { connectDB } = require('./config/connectdb')
const errorHandler = require('./middleware/error')
const app = express()
const port = process.env.PORT || 8080

app.use(express.json())
app.use(express.urlencoded({ extended: true }))

connectDB()

app.use('/api', userRoutes)
app.use('/api/hospitals', hospitalRoutes)
app.use('/api/history', historyRoutes)

app.use(errorHandler)

app.listen(port, () => console.log(`Server started on port ${port}.`))