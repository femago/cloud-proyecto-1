import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Application } from './application.model';
import { BaseEntity, createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Application>;

@Injectable()
export class ApplicationService {

    private resourceUrl =  SERVER_API_URL + 'api/applications';

    currentContest: BaseEntity = null;

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(application: Application): Observable<EntityResponseType> {
        const copy = this.convert(application);
        return this.http.post<Application>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(application: Application): Observable<EntityResponseType> {
        const copy = this.convert(application);
        return this.http.put<Application>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Application>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Application[]>> {
        const options = createRequestOption(req);
        return this.http.get<Application[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Application[]>) => this.convertArrayResponse(res));
    }

    queryPublicApplicationsByContest(contestId: number, req?: any): Observable<HttpResponse<Application[]>> {
        const options = createRequestOption(req);
        return this.http.get(`${this.resourceUrl}/contests/${contestId}`, { params: options, observe: 'response' })
            .map((res: HttpResponse<Application[]>) => this.convertArrayResponse(res));
    }
    queryPrivateApplicationsByContest(contestId: number, req?: any): Observable<HttpResponse<Application[]>> {
        const options = createRequestOption(req);
        return this.http.get(`${this.resourceUrl}/contests/${contestId}/principal`, { params: options, observe: 'response' })
            .map((res: HttpResponse<Application[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Application = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Application[]>): HttpResponse<Application[]> {
        const jsonResponse: Application[] = res.body;
        const body: Application[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Application.
     */
    private convertItemFromServer(application: Application): Application {
        const copy: Application = Object.assign({}, application);
        copy.createDate = this.dateUtils
            .convertDateTimeFromServer(application.createDate);
        return copy;
    }

    /**
     * Convert a Application to a JSON which can be sent to the server.
     */
    private convert(application: Application): Application {
        const copy: Application = Object.assign({}, application);

        copy.createDate = this.dateUtils.toDate(application.createDate);
        return copy;
    }

    originalVoiceResolver(id: number) {
        return `${this.resourceUrl}/${id}/voice/original`;
    }

    convertedVoiceResolver(id: number) {
        return `${this.resourceUrl}/${id}/voice/converted`;
    }
}
