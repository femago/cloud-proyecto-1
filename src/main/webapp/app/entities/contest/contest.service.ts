import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Contest } from './contest.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Contest>;

@Injectable()
export class ContestService {

    private resourceUrl =  SERVER_API_URL + 'api/contests';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(contest: Contest): Observable<EntityResponseType> {
        const copy = this.convert(contest);
        return this.http.post<Contest>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(contest: Contest): Observable<EntityResponseType> {
        const copy = this.convert(contest);
        return this.http.put<Contest>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Contest>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    findByUUID(uuid: number): Observable<EntityResponseType> {
        return this.http.get<Contest>(`${this.resourceUrl}/uuid/${uuid}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    // query(req?: any): Observable<HttpResponse<Contest[]>> {
    //     const options = createRequestOption(req);
    //     return this.http.get<Contest[]>(this.resourceUrl, { params: options, observe: 'response' })
    //         .map((res: HttpResponse<Contest[]>) => this.convertArrayResponse(res));
    // }

    queryOwned(req?: any): Observable<HttpResponse<Contest[]>> {
        console.log('llamando concursos por principal');
        const options = createRequestOption(req);
        return this.http.get<Contest[]>(`${this.resourceUrl}/principal`, { params: options, observe: 'response' })
            .map((res: HttpResponse<Contest[]>) => this.convertArrayResponse(res));
    }

    queryPublished(req?: any): Observable<HttpResponse<Contest[]>> {
        console.log('llamando todos los concursos');
        const options = createRequestOption(req);
        return this.http.get<Contest[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Contest[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Contest = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Contest[]>): HttpResponse<Contest[]> {
        const jsonResponse: Contest[] = res.body;
        const body: Contest[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Contest.
     */
    private convertItemFromServer(contest: Contest): Contest {
        const copy: Contest = Object.assign({}, contest);
        copy.createDate = this.dateUtils
            .convertDateTimeFromServer(contest.createDate);
        copy.startDate = this.dateUtils
            .convertLocalDateFromServer(contest.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateFromServer(contest.endDate);
        return copy;
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
