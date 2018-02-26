import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Contest } from './contest.model';
import { ContestService } from './contest.service';
import {CONTEST_CONTENT_MODE} from '../../shared';

@Component({
    selector: 'jhi-contest-detail',
    templateUrl: './contest-detail.component.html'
})
export class ContestDetailComponent implements OnInit, OnDestroy {

    contest: Contest;
    contentMode: string;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private contestService: ContestService,
        private activatedRoute: ActivatedRoute,
        private route: ActivatedRoute
    ) {
        this.activatedRoute.data.subscribe((data) => {
            this.contentMode = data.contentMode;
            console.log('Data de la ruta', data)
        });
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInContests();
    }

    load(id) {
        let contestObservable;
        if (this.isPrivate()) {
            contestObservable = this.contestService.find(id);
        } else if (this.isPublic()) {
            contestObservable = this.contestService.findByUUID(id);
        } else {
            throw new Error(`Invalid mode to query contests ${this.contentMode}`);
        }
        contestObservable.subscribe((contest) => {
            this.contest = contest;
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

    isPrivate() {
        return this.contentMode === CONTEST_CONTENT_MODE.private;
    }
    isPublic() {
        return this.contentMode === CONTEST_CONTENT_MODE.public;
    }
}
