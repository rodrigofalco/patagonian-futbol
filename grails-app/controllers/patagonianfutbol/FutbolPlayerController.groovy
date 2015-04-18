package patagonianfutbol

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class FutbolPlayerController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond FutbolPlayer.list(params), model:[futbolPlayerCount: FutbolPlayer.count()]
    }

    def show(FutbolPlayer futbolPlayer) {
        respond futbolPlayer
    }

    def create() {
        respond new FutbolPlayer(params)
    }

    @Transactional
    def save(FutbolPlayer futbolPlayer) {
        if (futbolPlayer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (futbolPlayer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond futbolPlayer.errors, view:'create'
            return
        }

        futbolPlayer.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'futbolPlayer.label', default: 'FutbolPlayer'), futbolPlayer.id])
                redirect futbolPlayer
            }
            '*' { respond futbolPlayer, [status: CREATED] }
        }
    }

    def edit(FutbolPlayer futbolPlayer) {
        respond futbolPlayer
    }

    @Transactional
    def update(FutbolPlayer futbolPlayer) {
        if (futbolPlayer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (futbolPlayer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond futbolPlayer.errors, view:'edit'
            return
        }

        futbolPlayer.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'futbolPlayer.label', default: 'FutbolPlayer'), futbolPlayer.id])
                redirect futbolPlayer
            }
            '*'{ respond futbolPlayer, [status: OK] }
        }
    }

    @Transactional
    def delete(FutbolPlayer futbolPlayer) {

        if (futbolPlayer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        futbolPlayer.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'futbolPlayer.label', default: 'FutbolPlayer'), futbolPlayer.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'futbolPlayer.label', default: 'FutbolPlayer'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
