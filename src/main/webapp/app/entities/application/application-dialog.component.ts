import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Observable';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiDataUtils, JhiEventManager} from 'ng-jhipster';

import {Application} from './application.model';
import {ApplicationPopupService} from './application-popup.service';
import {ApplicationService} from './application.service';
import {Contest, ContestService} from '../contest';
import {ResponseWrapper} from '../../shared';

@Component({
    selector: 'jhi-application-dialog',
    templateUrl: './application-dialog.component.html'
})
export class ApplicationDialogComponent implements OnInit, OnDestroy {

    application: Application;
    isSaving: boolean;

    contests: Contest[];
    contest: Contest;

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private applicationService: ApplicationService,
        private contestService: ContestService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.contest = this.applicationService.currentContest;
        this.contestService.query()
            .subscribe((res: ResponseWrapper) => { this.contests = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    ngOnDestroy(): void {
        this.applicationService.currentContest = null;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;

        if (this.contest !== undefined) {
            this.application.contest = this.contest;
        }

        if (this.application.id !== undefined) {
            this.subscribeToSaveResponse(
                this.applicationService.update(this.application));
        } else {
            this.subscribeToSaveResponse(
                this.applicationService.create(this.application));
        }
    }

    private subscribeToSaveResponse(result: Observable<Application>) {
        result.subscribe((res: Application) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Application) {
        this.eventManager.broadcast({ name: 'applicationListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackContestById(index: number, item: Contest) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-application-popup',
    template: ''
})
export class ApplicationPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private applicationPopupService: ApplicationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.applicationPopupService
                    .open(ApplicationDialogComponent as Component, params['id']);
            } else {
                this.applicationPopupService
                    .open(ApplicationDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
