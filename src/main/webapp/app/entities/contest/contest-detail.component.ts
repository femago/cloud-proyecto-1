import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Contest } from './contest.model';
import { ContestService } from './contest.service';

@Component({
    selector: 'jhi-contest-detail',
    templateUrl: './contest-detail.component.html'
})
export class ContestDetailComponent implements OnInit, OnDestroy {

    contest: Contest;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private contestService: ContestService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInContests();
    }

    load(id) {
        this.contestService.find(id)
            .subscribe((contestResponse: HttpResponse<Contest>) => {
                this.contest = contestResponse.body;
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

    registerChangeInContests() {
        this.eventSubscriber = this.eventManager.subscribe(
            'contestListModification',
            (response) => this.load(this.contest.id)
        );
    }
}
