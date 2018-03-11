import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Contest } from './contest.model';
import { ContestPopupService } from './contest-popup.service';
import { ContestService } from './contest.service';

@Component({
    selector: 'jhi-contest-delete-dialog',
    templateUrl: './contest-delete-dialog.component.html'
})
export class ContestDeleteDialogComponent {

    contest: Contest;

    constructor(
        private contestService: ContestService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.contestService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'contestListModification',
                content: 'Deleted an contest'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-contest-delete-popup',
    template: ''
})
export class ContestDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contestPopupService: ContestPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.contestPopupService
                .open(ContestDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
