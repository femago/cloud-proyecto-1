import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { Contest } from './contest.model';
import { ContestService } from './contest.service';
import { CONTEST_CONTENT_MODE, ITEMS_PER_PAGE, Principal } from '../../shared';

@Component({
    selector: 'jhi-contest',
    templateUrl: './contest.component.html'
})
export class ContestComponent implements OnInit, OnDestroy {

currentAccount: any;
    contests: Contest[];
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
    contentMode: string;

    constructor(
        private contestService: ContestService,
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
            this.predicate = 'createDate';

            this.contentMode = data.contentMode;
            console.log('Data de la ruta', data);
        });
    }
    isPrivate() {
        return this.contentMode === CONTEST_CONTENT_MODE.private;
    }
    isPublic() {
        return this.contentMode === CONTEST_CONTENT_MODE.public;
    }

    loadAll() {
        let contestResponse;
        const rqParams = {
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()};

        if (this.isPrivate()) {
            contestResponse = this.contestService.queryOwned(rqParams);
        } else if (this.isPublic()) {
            contestResponse = this.contestService.queryPublished(rqParams);
        } else {
            throw new Error(`Invalid mode to query contests ${this.contentMode}`);
        }

        contestResponse.subscribe(
            (res: HttpResponse<Contest[]>) => this.onSuccess(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate([this.routeByMode()], {queryParams:
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
        this.router.navigate([this.routeByMode(), {
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
        this.registerChangeInContests();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Contest) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInContests() {
        this.eventSubscriber = this.eventManager.subscribe('contestListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    routeToContestDetail(id) {
        if (this.isPrivate()) {
            this.router.navigate(['../contest', id ]);
        }else if (this.isPublic()) {
            this.router.navigate(['../contest-open', id ]);
        }else {
            throw new Error(`Invalid mode to query contests ${this.contentMode}`);
        }
    }
    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.contests = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
    private routeByMode() {
        if (this.isPrivate()) {
            return '/contest';
        } else if (this.isPublic()) {
            return '/contest-open';
        } else {
            throw new Error(`Invalid mode to query contests ${this.contentMode}`);
        }
    }
}
