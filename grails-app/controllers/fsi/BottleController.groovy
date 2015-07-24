package fsi

import org.springframework.dao.DataIntegrityViolationException

class BottleController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [bottleInstanceList: Bottle.list(params), bottleInstanceTotal: Bottle.count()]
    }

    def create() {
        [bottleInstance: new Bottle(params)]
    }

    def save() {
        def bottleInstance = new Bottle(params)
        if (!bottleInstance.save(flush: true)) {
            render(view: "create", model: [bottleInstance: bottleInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'bottle.label', default: 'Bottle'), bottleInstance.id])
        redirect(action: "show", id: bottleInstance.id)
    }

    def show(Long id) {
        def bottleInstance = Bottle.get(id)
        if (!bottleInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bottle.label', default: 'Bottle'), id])
            redirect(action: "list")
            return
        }

        [bottleInstance: bottleInstance]
    }

    def edit(Long id) {
        def bottleInstance = Bottle.get(id)
        if (!bottleInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bottle.label', default: 'Bottle'), id])
            redirect(action: "list")
            return
        }

        [bottleInstance: bottleInstance]
    }

    def update(Long id, Long version) {
        def bottleInstance = Bottle.get(id)
        if (!bottleInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bottle.label', default: 'Bottle'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (bottleInstance.version > version) {
                bottleInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'bottle.label', default: 'Bottle')] as Object[],
                          "Another user has updated this Bottle while you were editing")
                render(view: "edit", model: [bottleInstance: bottleInstance])
                return
            }
        }

        bottleInstance.properties = params

        if (!bottleInstance.save(flush: true)) {
            render(view: "edit", model: [bottleInstance: bottleInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'bottle.label', default: 'Bottle'), bottleInstance.id])
        redirect(action: "show", id: bottleInstance.id)
    }

    def delete(Long id) {
        def bottleInstance = Bottle.get(id)
        if (!bottleInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'bottle.label', default: 'Bottle'), id])
            redirect(action: "list")
            return
        }

        try {
            bottleInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'bottle.label', default: 'Bottle'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'bottle.label', default: 'Bottle'), id])
            redirect(action: "show", id: id)
        }
    }
}
