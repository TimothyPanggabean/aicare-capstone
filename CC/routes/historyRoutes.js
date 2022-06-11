const express = require('express')
const router = express.Router()
const {
    getHistory,
    addHistory
} = require('../controllers/historyController')
const { authorize } = require('../middleware/auth')
const Multer = require('multer')
const { uploadImage } = require('../middleware/imageUpload')

const upload = Multer({
    storage: Multer.memoryStorage()
})

router.route('/').get(authorize, getHistory).post(authorize, upload.single('image'), uploadImage, addHistory)

module.exports = router