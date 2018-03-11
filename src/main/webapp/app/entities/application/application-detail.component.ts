import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Application } from './application.model';
import { ApplicationService } from './application.service';

@Component({
    selector: 'jhi-application-detail',
    templateUrl: './application-detail.component.html'
})
export class ApplicationDetailComponent implements OnInit, OnDestroy {

    application: Application;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private applicationService: ApplicationService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInApplications();
    }

    load(id) {
        this.applicationService.find(id)
            .subscribe((applicationResponse: HttpResponse<Application>) => {
                this.application = applicationResponse.body;
            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInApplications() {
        this.eventSubscriber = this.eventManager.subscribe(
            'applicationListModification',
            (response) => this.load(this.application.id)
        );
    }

    downloadVoice() {
        const tab = window.open(this.applicationService.originalVoiceResolver(this.application.id));
    }

    resolveConvertedVoice() {
        return this.applicationService.convertedVoiceResolver(this.application.id);
    }

    displayPlayer() {
        return this.application.status.toString() === 'CONVERTED';
    }
}
