const { Storage } = require('@google-cloud/storage')
const fs = require('fs')
const dateFormat = require('dateformat')

const gcs = new Storage({
    projectId: 'aicare-development'
})

const bucketName = 'aicare-bucket'
const IMAGE_DIR = 'aicare-ml/images/'
const PROFILE_DIR = 'profile-pictures/'
const bucket = gcs.bucket(bucketName)

function getPublicUrl(filename) {
    return 'https://storage.googleapis.com/' + bucketName + '/' + filename;
}

const uploadImage = (req, res, next) => {
    if (req.file !== undefined){
        const gcsname = IMAGE_DIR + 'image-' + dateFormat(new Date(), "yyyymmdd-HHMMss")
        const file = bucket.file(gcsname)
    
        const stream = file.createWriteStream({
            metadata: {
                contentType: req.file.mimetype
            }
        })
    
        stream.on('error', (err) => {
            req.file.cloudStorageError = err
            console.log(err)
            next()
        })
    
        stream.on('finish', () => {
            req.file.cloudStorageObject = gcsname
            req.file.cloudStoragePublicUrl = getPublicUrl(gcsname)
            next()
        })
    
        stream.end(req.file.buffer)
    } else next()
}

const uploadProfile = (req, res, next) => {
    if (!req.file) res.status(400).json({ message: 'No photo uploaded.' })

    const gcsname = PROFILE_DIR + 'image-' + dateFormat(new Date(), "yyyymmdd-HHMMss")
    const file = bucket.file(gcsname)

    const stream = file.createWriteStream({
        metadata: {
            contentType: req.file.mimetype
        }
    })

    stream.on('error', (err) => {
        req.file.cloudStorageError = err
        console.log(err)
        next()
    })

    stream.on('finish', () => {
        req.file.cloudStorageObject = gcsname
        req.file.cloudStoragePublicUrl = getPublicUrl(gcsname)
        next()
    })

    stream.end(req.file.buffer)

}

module.exports = { uploadImage, uploadProfile }