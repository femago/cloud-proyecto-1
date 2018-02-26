import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Contest } from './contest.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ContestService {

    private resourceUrl =  SERVER_API_URL + 'api/contests';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(contest: Contest): Observable<Contest> {
        const copy = this.convert(contest);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(contest: Contest): Observable<Contest> {
        const copy = this.convert(contest);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Contest> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    queryOwned(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(`${this.resourceUrl}/principal`, options)
            .map((res: Response) => this.convertResponse(res));
    }

    queryPublished(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Contest.
     */
    private convertItemFromServer(json: any): Contest {
        const entity: Contest = Object.assign(new Contest(), json);
        entity.createDate = this.dateUtils
            .convertDateTimeFromServer(json.createDate);
        entity.startDate = this.dateUtils
            .convertLocalDateFromServer(json.startDate);
        entity.endDate = this.dateUtils
            .convertLocalDateFromServer(json.endDate);
        return entity;
    }

    /**
     * Convert a Contest to a JSON which can be sent to the server.
     */
    private convert(contest: Contest): Contest {
        const copy: Contest = Object.assign({}, contest);

        copy.createDate = this.dateUtils.toDate(contest.createDate);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(contest.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(contest.endDate);
        return copy;
    }
}
