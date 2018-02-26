import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';
import {JhiAlertService, JhiDataUtils, JhiEventManager, JhiParseLinks} from 'ng-jhipster';

import {Application} from './application.model';
import {ApplicationService} from './application.service';
import {CONTEST_CONTENT_MODE, ITEMS_PER_PAGE, Principal, ResponseWrapper} from '../../shared';
import {Contest} from '../contest/contest.model';

@Component({
    selector: 'jhi-application-table',
    templateUrl: './application-table.component.html'
})
export class ApplicationTableComponent implements OnInit, OnDestroy {

    @Input()
    contest: Contest;
    @Input()
    contentMode: string;
    currentAccount: any;
    applications: Application[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    sources: Array<Object> = [
            {
                src: 'http://static.videogular.com/assets/audios/videogular.mp3',
                type: 'audio/mp3'
            }
        ];

    constructor(
        private applicationService: ApplicationService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private dataUtils: JhiDataUtils,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = false;
            this.predicate = 'createDate'
        });
    }

    loadAll() {

        const rqParams = {
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()
        };
        let applicationsObservable;
        if (this.isPrivate()) {
            applicationsObservable = this.applicationService.queryPrivateApplicationsByContest(this.contest.id, rqParams);
        } else if (this.isPublic()) {
            applicationsObservable = this.applicationService.queryPublicApplicationsByContest(this.contest.id, rqParams);
        } else {
            throw new Error(`Invalid mode to query applications ${this.contentMode}`);
        }
        applicationsObservable.subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/contest', this.contest.id], {queryParams:
                {
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
                }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate(['/application', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInApplications();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Application) {
        return item.id;
    }

    newApplication() {
        this.applicationService.currentContest = this.contest;
        this.router.navigate(['/', {outlets: {popup: ['application-new']}}]);
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInApplications() {
        this.eventSubscriber = this.eventManager.subscribe('applicationListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    displayPlayer(applicationRow: Application) {
        return applicationRow.status.toString() === 'CONVERTED';
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.applications = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
    isPrivate() {
        return this.contentMode === CONTEST_CONTENT_MODE.private;
    }
    isPublic() {
        return this.contentMode === CONTEST_CONTENT_MODE.public;
    }
}
