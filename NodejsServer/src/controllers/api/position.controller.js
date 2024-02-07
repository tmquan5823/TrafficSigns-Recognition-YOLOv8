const { addPosition, showAllPositionById } = require('../CRUD/position')

const getListPositionById = async (req, res) => {
    try {
        const listPosition = await showAllPositionById(req.body.code)
        res.status(200).json({
            listPosition: listPosition
        })
    } catch (error) {
        if (error.errors) {
            error.errors.forEach((err) => {
                console.error(err.message);
            });
        }

        res.status(400).json({
            message: "Error Show List Position By Id!!!",
            error: error.message,
        });
    }
}

const createPosition = async (req, res) => {
    try {
        const listPosition = req.body.listPosition
        let length = listPosition.length
        for (let i = 0; i < length; i++) {
            const newPosition = {
                code: listPosition[i].code,
                longtitude: listPosition[i].longtitude,
                latitude: listPosition[i].latitude,
            }
            await addPosition(newPosition)
        }
        res.status(200).json({
            message: "Create all position successfully!!"
        })
    } catch (error) {
        if (error.errors) {
            error.errors.forEach((err) => {
                console.error(err.message);
            });
        }

        res.status(400).json({
            message: "Error Create Position!!!",
            error: error.message,
        });
    }
}

module.exports = {
    getListPositionById: getListPositionById,
    createPosition: createPosition
}