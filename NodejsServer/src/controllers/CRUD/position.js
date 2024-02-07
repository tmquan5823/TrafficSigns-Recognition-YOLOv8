const models = require('../../models');

const createPosition = async (newPosition) => {
    try {
        await models.Position.create(newPosition);
    } catch (error) {
        console.error("Error creating Position:", error);
        throw error; // Rethrow the error to let the caller handle it
    }
};

const showAllPositionById = async (id) => {
    try {
        return await models.Position.findAll({
            where: { deletedAt: null, code: id },
            attributes: [
                'code',
                'longtitude',
                'latitude'
            ]
        })
    } catch (error) {
        console.error("Error getting all position by id:", error);
        throw error; // Rethrow the error to let the caller handle it
    }
}

module.exports = {
    addPosition: createPosition,
    showAllPositionById: showAllPositionById
}
