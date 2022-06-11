const express = require('express')
const router = express.Router()
const {
    getAllHospitals,
    addHospital
} = require('../controllers/hospitalController')
// const { protect } = require('../middleware/auth')

router.route('/').get(getAllHospitals).post(addHospital)

module.exports = router